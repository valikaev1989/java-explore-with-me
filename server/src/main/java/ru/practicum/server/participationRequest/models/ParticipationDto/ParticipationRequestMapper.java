package ru.practicum.server.participationRequest.models.ParticipationDto;

import ru.practicum.server.participationRequest.models.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto toDtoRequest(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getRequestId())
                .event(participationRequest.getEvent().getEventId())
                .requester(participationRequest.getUser().getUserId())
                .status(participationRequest.getState().toString())
                .build();
    }

    public static List<ParticipationRequestDto> requestDtoList(List<ParticipationRequest> requests) {
        return requests.stream().map(ParticipationRequestMapper::toDtoRequest).collect(Collectors.toList());
    }
}