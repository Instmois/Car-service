package ru.unn.autorepairshop.domain.mapper.partorder;

import org.springframework.stereotype.Component;
import ru.unn.autorepairshop.domain.dto.response.PartOrderResponseDto;
import ru.unn.autorepairshop.domain.entity.PartOrder;
import ru.unn.autorepairshop.domain.enums.PartOrderStatus;

import java.time.format.DateTimeFormatter;

@Component
public class PartOrderResponseDtoMapper {

    private final static String DEFAULT_FIELD_STATUS = "В обработке";

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public PartOrderResponseDto mapPartOrderToDto(PartOrder partOrder) {
        if (partOrder.getStatus() == PartOrderStatus.NEED_TO_ORDER) {
            return new PartOrderResponseDto(
                    partOrder.getId(),
                    partOrder.getPartName(),
                    partOrder.getAmount(),
                    DEFAULT_FIELD_STATUS,
                    DEFAULT_FIELD_STATUS,
                    partOrder.getPrice(),
                    partOrder.getStatus()
            );
        } else {
            return new PartOrderResponseDto(
                    partOrder.getId(),
                    partOrder.getPartName(),
                    partOrder.getAmount(),
                    partOrder.getOrderDate().format(DATE_TIME_FORMATTER),
                    partOrder.getDeliveryDate().format(DATE_TIME_FORMATTER),
                    partOrder.getPrice(),
                    partOrder.getStatus()
            );
        }
    }

}
