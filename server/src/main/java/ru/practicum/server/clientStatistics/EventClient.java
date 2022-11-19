package ru.practicum.server.clientStatistics;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.server.clientStatistics.models.EndpointDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.server.utils.FormatDate.FORMATTER;

@Slf4j
@Service
public class EventClient extends BaseClient {

    @Autowired
    public EventClient(@Value("${STAT-SERVER-URL}") String serverUrl, RestTemplateBuilder builder, Gson gson) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public void postStats(EndpointDto endpointDto) {
        log.info("postStats start: endpointDto: {}", endpointDto);
        post("/hit", endpointDto);
        log.info("postStats end: ok");
    }

    public Long getViews(Long eventId) {
        log.info("getViews start: eventId: {}", eventId);
        String url = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        Map<String, Object> parameters = Map.of(
                "start", URLEncoder.encode(LocalDateTime.now()
                        .minusYears(100).format(FORMATTER), StandardCharsets.UTF_8),
                "end", URLEncoder.encode(LocalDateTime.now().format(FORMATTER), StandardCharsets.UTF_8),
                "uris", "/events/" + eventId,
                "unique", "false"
        );
        ResponseEntity<Object> response = get(url, parameters);
        List<LinkedHashMap> collection = (List<LinkedHashMap>) response.getBody();
        if (!Objects.requireNonNull(collection).isEmpty()) {
            return Long.valueOf((Integer) collection.get(0).get("hits"));
        }
        return 0L;
    }
}