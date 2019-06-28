package com.netcracker.denisik.services;

import java.util.List;

public interface CrudService<T> {
    long add(T t);

    void delete(long id);

    List<T> getAll();

    T get(long id);

    void convertToXml(List<T> t);

    void convertToJson(List<T> t);

    void convertToExcel(List<T> t);
}
