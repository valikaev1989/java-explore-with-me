package ru.practicum.server.clientStatistics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.server.clientStatistics.models.EndpointDto;
import ru.practicum.server.clientStatistics.models.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.server.utils.FormatDate.FORMATTER;

@Slf4j
@Service
public class EventClient extends BaseClient {
    @Autowired
    public EventClient(@Value("${STAT-SERVER-URL}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public void postStats(EndpointDto endpointDto) {
        post("/hit", endpointDto);
    }

    public Long getViews(Long eventId) {
        String url = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        Map<String, Object> parameters = Map.of(
                "start", URLEncoder.encode(LocalDateTime.now()
                        .minusYears(100).format(FORMATTER), StandardCharsets.UTF_8),
                "end", URLEncoder.encode(LocalDateTime.now().format(FORMATTER), StandardCharsets.UTF_8),
                "uris", (List.of("/events/" + eventId)),
                "unique", "false"
        );
        ResponseEntity<Object> response = get(url, parameters);
        log.info("EventClient.getViews response: {}", response);
        List<ViewStats> viewStatsList = response.hasBody() ? (List<ViewStats>) response.getBody() : List.of();
        return viewStatsList != null && !viewStatsList.isEmpty() ? viewStatsList.get(0).getHits() : 0L;
    }
}