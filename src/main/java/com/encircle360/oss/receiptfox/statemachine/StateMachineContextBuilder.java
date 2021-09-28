package com.encircle360.oss.receiptfox.statemachine;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.support.DefaultStateMachineContext;

/**
 * Use this builder to create an suitable StateMachineContext without
 * having issues, selecting the right constructor
 *
 * @param <S>
 * @param <E>
 */
public class StateMachineContextBuilder<S,E> {

    private String id;

    private S state;

    private StateMachineContextBuilder() {
    }

    public static <S,E> StateMachineContextBuilder<S, E> create(Class<S> stateClass, Class<E> eventClass) {
        return new StateMachineContextBuilder<>();
    }

    public StateMachineContextBuilder<S, E> state(S state) {
        this.state = state;
        return this;
    }

    public StateMachineContextBuilder<S, E> id(String id) {
        this.id = id;
        return this;
    }

    public StateMachineContext<S, E> build() {
        return new DefaultStateMachineContext<>(
                null,
                state,
                null,
                null,
                null,
                null,
                id);
    }
}
