package fade.inject.event.events;

import fade.inject.Ignore;
import fade.inject.event.Handler;
import fade.inject.event.Manager;
import fade.inject.event.exception.PossibleMissingAnnotationException;
import fade.inject.event.exception.PossibleMissingHandlerMethodsException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationTest {

    @Test
    @DisplayName("throw if handler class has no methods")
    void testThrowIfHandlerClassHasNoMethods() {
        Manager manager = Manager.builder().build();
        assertThrows(PossibleMissingAnnotationException.class, () -> manager.register(new Object() {
            @SuppressWarnings("unused") // intentional
            public void handle(@NotNull StringEvent event, @NotNull StringEvent.StringEventContext context) {
                context.setString("B");
            }
        }));

        StringEvent event = new StringEvent("A");
        manager.invoke(event);

        assertNotEquals("B", event.getContext().getString());
    }

    @Test
    @DisplayName("do not throw if handler is ignored")
    void testDoNotThrowIfHandlerIsIgnored() {
        Manager manager = Manager.builder().build();

        // noinspection AnonymousInnerClassWithTooManyMethods
        assertDoesNotThrow(() -> manager.register(new Object() {
            @Ignore
            public void handle(@NotNull StringEvent event) {
                event.getContext().setString("C");
            }

            @Handler
            public void handle(@NotNull StringEvent event, @NotNull StringEvent.StringEventContext context) {
                context.setString("B");
            }
        }));

        StringEvent event = new StringEvent("A");
        manager.invoke(event);

        assertEquals("B", event.getContext().getString());
    }

    @Test
    @DisplayName("do not throw if handler class has only ignored handlers")
    void testDoNotThrowIfHandlerClassHasOnlyIgnoredHandlers() {
        Manager manager = Manager.builder().build();

        assertDoesNotThrow(() -> manager.register(new Object() {
            @Handler
            @Ignore
            public void handle(@NotNull StringEvent event, @NotNull StringEvent.StringEventContext context) {
                context.setString("B");
            }
        }));

        StringEvent event = new StringEvent("A");
        manager.invoke(event);

        assertNotEquals("B", event.getContext().getString());
    }

    @Test
    @DisplayName("do not register handlers of ignored handler class")
    void testDoNotRegisterHandlersOfIgnoredHandlerClass() {
        Manager manager = Manager.builder().build();

        @Ignore
        final class IgnoredHandler {

            @Handler
            public void handle(@NotNull StringEvent event, @NotNull StringEvent.StringEventContext context) {
                context.setString("B");
            }
        }
        assertDoesNotThrow(() -> manager.register(new IgnoredHandler()));

        StringEvent event = new StringEvent("A");
        manager.invoke(event);

        assertNotEquals("B", event.getContext().getString());
    }

    @Test
    @DisplayName("do not throw if handler class has only ignored handler-like methods")
    void testDoNotThrowIfHandlerClassHasOnlyIgnoredHandlerLikeMethods() {
        Manager manager = Manager.builder().build();
        assertDoesNotThrow(() -> manager.register(new Object() {
            @Ignore
            public void handle(@NotNull StringEvent event) {
                event.getContext().setString("B");
            }
        }));

        StringEvent event = new StringEvent("A");
        manager.invoke(event);

        assertNotEquals("B", event.getContext().getString());
    }

    @Test
    @DisplayName("throw if handler class has no handler methods")
    void testThrowIfHandlerClassHasNoHandlerMethods() {
        Manager manager = Manager.builder().build();
        assertThrows(PossibleMissingHandlerMethodsException.class, () -> manager.register(new Object()));

        StringEvent event = new StringEvent("A");
        manager.invoke(event);

        assertEquals("A", event.getContext().getString());
    }
}
