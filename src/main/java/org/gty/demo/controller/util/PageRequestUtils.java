package org.gty.demo.controller.util;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.StreamSupport;

public final class PageRequestUtils {

    private PageRequestUtils() {
    }

    @Nonnull
    public static PageRequest constructPageRequest(final int page,
                                                   final int size,
                                                   @Nonnull final String sort) {
        final var tempIterable = Splitter.onPattern(",")
            .trimResults()
            .omitEmptyStrings()
            .split(Objects.requireNonNull(sort, "sort must not be null"));

        final var tempArray = StreamSupport.stream(tempIterable.spliterator(), true)
            .toArray(String[]::new);

        if (tempArray.length != 2) {
            throw new IllegalArgumentException("Unable to resolve SQL sort parameters: " + sort);
        }

        final var property = tempArray[0];
        final var order = tempArray[1];

        Sort.Order orderObject = null;
        if (StringUtils.equals(order, "asc")) {
            orderObject = Sort.Order.asc(property);
        } else if (StringUtils.equals(order, "desc")) {
            orderObject = Sort.Order.desc(property);
        }

        if (orderObject == null) {
            throw new IllegalArgumentException("Unable to resolve SQL sort parameters: " + sort);
        }

        return PageRequest.of(page, size, Sort.by(orderObject));
    }
}
