package ru.practicum.server.participationRequest.models.ParticipationDto;

import ru.practicum.server.participationRequest.models.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.utils.FormatDate.FORMATTER;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto toDtoRequest(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getRequestId())
                .event(participationRequest.getEvent().getEventId())
                .requester(participationRequest.getUser().getUserId())
                .created(participationRequest.getCreated().format(FORMATTER))
                .status(participationRequest.getStatus().toString())
                .build();
    }

    public static List<ParticipationRequestDto> requestDtoList(List<ParticipationRequest> requests) {
        return requests.stream().map(ParticipationRequestMapper::toDtoRequest).collect(Collectors.toList());
    }
}