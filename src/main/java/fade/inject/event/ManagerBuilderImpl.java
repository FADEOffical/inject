package fade.inject.event;

import org.jetbrains.annotations.NotNull;

public final class ManagerBuilderImpl implements Manager.ManagerBuilder {

    private ManagerBuilderImpl() {}

    static ManagerBuilderImpl create() {
        return new ManagerBuilderImpl();
    }

    @Override
    public @NotNull Manager build() {
        return new ManagerImpl();
    }
}
