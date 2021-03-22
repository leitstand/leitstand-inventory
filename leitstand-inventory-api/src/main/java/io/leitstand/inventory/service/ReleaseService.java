package io.leitstand.inventory.service;

import java.util.List;

/**
 * A release management service.
 * <p>
 * The <code>ReleaseService</code> allows querying, storing and removing releases.
 */
public interface ReleaseService {

    /**
     * Searches release by their name.
     * @param filter a regular expression to filter releases by their name.
     * @return all matching releases ordered by release name.
     */
    List<ReleaseSettings> findReleases(String filter);
    
    /**
     * Returns the release settings.
     * @param releaseId the release ID
     * @return the release settings.
     * @throws EntityNotFoundException if the release does not exist.
     */
    ReleaseSettings getRelease(ReleaseId releaseId);

    /**
     * Returns the release settings.
     * @param releaseName the release name
     * @return the release settings.
     * @throws EntityNotFoundException if the release does not exist.
     */
    ReleaseSettings getRelease(ReleaseName releaseName);

    /**
     * Stores the release settings.
     * @param settings the release settings
     * @return <code>true</code> if a new release is created, <code>false</code> otherwise.
     */
    boolean storeRelease(ReleaseSettings settings);
    
    /**
     * Removes a release.
     * @param releaseId the release ID.
     */
    void removeRelease(ReleaseId releaseId);
    
    /**
     * Removes a release.
     * @param releaseName the release name.
     */
    void removeRelease(ReleaseName releaseName);
}
