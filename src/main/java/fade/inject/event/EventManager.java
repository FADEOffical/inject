package fade.inject.event;

import fade.inject.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * An event manager is a class that holds a reference to all handler methods. It can register handlers and invoke them
 * by calling the {@link EventManager#invoke(Event)} method.
 *

 */
public interface EventManager {

    static @NotNull EventManager create() {
        return EventManager.builder().build();
    }

    static @NotNull EventManager.ManagerBuilder builder() {
        return ManagerBuilderImpl.create();
    }

    void register(@NotNull Object handler);

    void unregister(@NotNull Object object);

    void unregister(@NotNull Class<?> handler);

    boolean isRegistered(@NotNull Class<?> handler);

    @NotNull Optional<Object> getHandler(@NotNull Class<?> handler);

    void invoke(@NotNull Event event);

    sealed interface ManagerBuilder extends Builder<EventManager> permits ManagerBuilderImpl {}
}
