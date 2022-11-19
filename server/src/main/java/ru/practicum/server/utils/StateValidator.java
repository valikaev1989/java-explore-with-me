package ru.practicum.server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.server.exception.models.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StateValidator {
    public List<State> getCorrectStateList(List<String> states) {
        log.info("Validation.getCorrectStateList start: states: {}", states);
        List<State> stateList = new ArrayList<>();
        if (states != null && !states.isEmpty()) {
            for (String state : states) {
                try {
                    stateList.add(State.valueOf(state));
                } catch (NotFoundException ex) {
                    log.warn(ex.getMessage());
                }
            }
        }
        log.info("Validation.getCorrectStateList end: stateList: {}", stateList);
        return stateList;
    }
}
