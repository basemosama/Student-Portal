package com.basemosama.studentportal.network.util;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.basemosama.studentportal.database.executor.AppExecutor;
import com.basemosama.studentportal.model.network.ApiResponse;
import com.basemosama.studentportal.model.network.ApiResult;

// CacheObject: Type for the Resource data. (database cache)
// RequestObject: Type for the API response. (network request)
public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();
    private AppExecutor appExecutor;

    public NetworkBoundResource() {
        appExecutor = AppExecutor.getExecutor();
        init();
    }


    private void init() {
        results.setValue((Resource<CacheObject>) Resource.loading(null));

        final LiveData<CacheObject> dbSource = loadFromDb();

        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                results.removeSource(dbSource);

                if (shouldFetch(cacheObject)) {
                    fetchFromNetwork(dbSource);
                } else {
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject cacheObject) {
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }


            }
        });
    }

    private void fetchFromNetwork(final LiveData<CacheObject> dbSource) {
        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                setValue((Resource.loading(cacheObject)));
            }
        });
        final LiveData<ApiResult<ApiResponse<RequestObject>>> apiResponse = createCall();

        results.addSource(apiResponse, new Observer<ApiResult<ApiResponse<RequestObject>>>() {
            @Override
            public void onChanged(final ApiResult<ApiResponse<RequestObject>> requestObjectResult) {
                results.removeSource(dbSource);
                results.removeSource(apiResponse);

                if (requestObjectResult instanceof ApiResult.Success) {
                    appExecutor.getDiskIo().execute(new Runnable() {
                        @Override
                        public void run() {
                            RequestObject requestObject = (RequestObject) processResponse((ApiResult.Success) requestObjectResult);
                            saveCallResult(requestObject);
                        }
                    });
                    appExecutor.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            results.addSource(dbSource, new Observer<CacheObject>() {
                                @Override
                                public void onChanged(CacheObject cacheObject) {
                                    setValue(Resource.success(cacheObject));
                                }
                            });
                        }
                    });
                } else if (requestObjectResult instanceof ApiResult.Empty) {
                    appExecutor.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            results.addSource(loadFromDb(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(CacheObject cacheObject) {
                                    setValue(Resource.success(cacheObject));
                                }
                            });
                        }
                    });
                } else if (requestObjectResult instanceof ApiResult.Error) {
                    results.addSource(loadFromDb(), new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject cacheObject) {
                            setValue(Resource.error(((ApiResult.Error) requestObjectResult).getError(), cacheObject));
                        }
                    });
                }
            }
        });

    }

    private CacheObject processResponse(ApiResult.Success response) {
        return (CacheObject) response.getData();
    }

    private void setValue(Resource<CacheObject> newValue) {
        if (results.getValue() != newValue) {
            results.setValue(newValue);
        }
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject items);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    // Called to get the cached data from the database.
    @NonNull
    @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResult<ApiResponse<RequestObject>>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<CacheObject>> getAsLiveData() {
        return results;
    }

}
