package com.lehuutri.studentmanagement.mappers;

public interface Mapper <TypeA, TypeB> {
    TypeB mapTo(TypeA a);
    TypeA mapFrom(TypeB b);
}
