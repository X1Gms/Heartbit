package com.heartbit.heartbit_project.network;

import java.util.List;

public interface MessageListener {
    void onHeartbeat(JsonHandler data);  // For regular heartbit/bpm messages
    void onOfflineData(List<DataPoint> dataPoints);
    void onCacheDetected(Boolean hasCache); // For heartbit/offline data batches
}