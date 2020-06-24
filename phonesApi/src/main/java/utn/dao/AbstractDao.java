package utn.dao;

import utn.exceptions.AlreadyExistsException;

public interface AbstractDao<T> {

    Object add(T value) throws AlreadyExistsException;

    void update(T value);

    void delete(Integer id);

    Object getById(Integer id);

}
