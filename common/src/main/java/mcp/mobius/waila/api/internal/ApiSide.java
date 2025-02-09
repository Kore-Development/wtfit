package mcp.mobius.waila.api.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jetbrains.annotations.ApiStatus;

public final class ApiSide {

    /**
     * All class or method that has this annotation means its feature will only be called on the client-side.
     * Therefore, you're safe to access client-only features in its implementation.
     * Though keep in mind that the class itself is still available on the server,
     * so make sure you're not accessing client features in a static context.
     */
    @ApiStatus.Internal
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.TYPE, ElementType.METHOD})
    public @interface ClientOnly {

    }

    /**
     * All class or method that has this annotation means its feature will only be called on the server-side.
     * Server-side in this case could be the integrated or the dedicated server one.
     */
    @ApiStatus.Internal
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.TYPE, ElementType.METHOD})
    public @interface ServerOnly {

    }

}
