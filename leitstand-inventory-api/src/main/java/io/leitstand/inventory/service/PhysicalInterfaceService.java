/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.List;

public interface PhysicalInterfaceService {

	List<PhysicalInterfaceData> findPhysicalInterfaces(String filter);

}
