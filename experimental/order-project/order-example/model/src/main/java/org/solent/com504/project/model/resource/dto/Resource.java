package org.solent.com504.project.model.resource.dto;

import java.util.List;
import org.solent.com504.project.model.party.dto.Party;

public class Resource {

    private String href;

    private String uuid;

    private String name;

    private List<Characteristic> characteristics;

    private Party resourceOwner;

    private ResourceAccess resourceController;

    private String resourceTypeName;

    private String description;
    
    
}
