package com.mapper;

import com.exception.DeletionRejectException;

import java.util.List;

public interface Mapper {
    public void delete(Long id) throws DeletionRejectException;
}
