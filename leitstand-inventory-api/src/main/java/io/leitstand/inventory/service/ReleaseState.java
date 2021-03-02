package io.leitstand.inventory.service;

/**
 * Enumeration of release lifecycle states.
 */
public enum ReleaseState {

    /** A candidate release piloted in the live network.*/
    CANDIDATE,
    /** The current release.*/
    RELEASE,
    /** A superseded release. Superseded releases form the release history.*/
    SUPERSEDED;
    
}
