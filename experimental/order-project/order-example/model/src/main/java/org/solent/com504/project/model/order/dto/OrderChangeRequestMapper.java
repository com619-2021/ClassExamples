/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.model.order.dto;

import java.util.List;
import org.solent.com504.project.model.resource.dto.*;
import org.solent.com504.project.model.party.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * maps one OrderChangeRequest into OrderChangeRequest can be used to map values of OrderChangeRequest entity to dto objects
 *
 * @author cgallen
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderChangeRequestMapper {

    OrderChangeRequestMapper INSTANCE = Mappers.getMapper(OrderChangeRequestMapper.class);

    OrderChangeRequestEntity orderToOrderChangeRequestEntity(OrderChangeRequest orderChangeRequest);

    OrderChangeRequest orderChangeRequestEntityToOrderChangeRequest(OrderChangeRequestEntity orderChangeRequestEntity);

    List<OrderChangeRequest> orderChangeRequestEntityListToOrderChangeRequestList(List< OrderChangeRequestEntity> orderChangeRequestEntityList);

    OrderChangeRequestEntity updateOrderChangeRequestEntity(OrderChangeRequest orderChangeRequest, @MappingTarget OrderChangeRequestEntity orderChangeRequestEntity);

    OrderChangeRequestEntity updateOrderChangeRequestEntityFromOrderEntity(OrderChangeRequestEntity newOrderChangeRequestEntity, @MappingTarget OrderChangeRequestEntity orderChangeRequestEntity);

}
