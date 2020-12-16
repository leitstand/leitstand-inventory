package io.leitstand.inventory.service;

import java.util.List;

import io.leitstand.inventory.service.ReleaseId;
import io.leitstand.inventory.service.ReleaseName;

public interface ReleaseService {

    List<ReleaseSettings> findReleases(String filter);
    ReleaseSettings getRelease(ReleaseId releaseId);
    ReleaseSettings getRelease(ReleaseName releaseName);
    void removeRelease(ReleaseId releaseId);
    void removeRelease(ReleaseName releaseName);
    boolean storeRelease(ReleaseSettings settings);
}
