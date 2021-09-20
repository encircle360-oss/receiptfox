package com.encircle360.oss.receiptfox.service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.encircle360.oss.receiptfox.dto.pagination.PaginationContainer;

@Service
public class PageContainerFactory {

    public <T> PageContainer<T> getPageContainer(final Pageable pageable, final Page<?> page, final List<T> content) {
        String sort = pageable.getSort()
            .toString()
            .replace(": ", ",");

        PaginationContainer pagination = PaginationContainer.builder()
            .sort(sort)
            .size(page.getSize())
            .page(page.getNumber())
            .totalElements((int) page.getTotalElements())
            .totalPages(page.getTotalPages())
            .first(page.isFirst())
            .last(page.isLast())
            .empty(page.isEmpty())
            .build();

        PageContainer<T> pageContainer = new PageContainer<>();
        pageContainer.setContent(content);
        pageContainer.setPagination(pagination);

        return pageContainer;
    }

    public Pageable mapRequestToPageable(final Integer size, final Integer page, final String sort) {
        int pageNumber = 0;
        int pageSize = 20;
        int maxPageSize = 1000;
        String sortKey = null;
        Sort sortBy = Sort.unsorted();

        if (size != null) {
            pageSize = size;
        }

        if (pageSize > maxPageSize) {
            pageSize = maxPageSize;
        }

        if (page != null) {
            pageNumber = page;
        }

        if (sort == null) {
            return PageRequest.of(pageNumber, pageSize, sortBy);
        }

        Sort.Direction direction = Sort.Direction.DESC;
        List<String> split = Arrays.asList(sort.split(","));
        Iterator<String> splitIterator = split.iterator();

        if (splitIterator.hasNext()) {
            sortKey = splitIterator.next();
        }

        if (splitIterator.hasNext()) {
            String sortDirection = splitIterator.next();
            direction = Sort.Direction.valueOf(sortDirection);
        }

        if (sortKey != null) {
            sortBy = Sort.by(direction, sortKey);
        }

        return PageRequest.of(pageNumber, pageSize, sortBy);
    }
}
