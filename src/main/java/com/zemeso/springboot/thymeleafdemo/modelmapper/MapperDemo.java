package com.zemeso.springboot.thymeleafdemo.modelmapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

public class MapperDemo {
    public static void main(String[] args) {
        ModelMapper model = new ModelMapper();


        Entity entity = new Entity();
        entity.setEmail("Santhosh.challa@gmail.com");
        entity.setFirstName("Santhosh");
        entity.setLastName("Challa");

        Dto dto = model.map(entity, Dto.class);
        System.out.println("Entity1 values: " + entity);
        System.out.println("DTO values: " + dto);


        Converter<Entity2, Dto> myConverter = new Converter<Entity2, Dto>()
        {
            public Dto convert(MappingContext<Entity2, Dto> context)
            {

                Entity2 source = context.getSource();
                Dto dto1 = context.getDestination();
                dto1.setFirstName(source.getFullName().split(" ")[0]);
                dto1.setLastName(source.getFullName().split(" ")[1]);
                dto1.setEmail(source.getEmail());
                return dto1;
            }
        };

        Converter<Dto,Entity2> myRevConverter = new Converter<Dto, Entity2>()
        {
            public Entity2 convert(MappingContext<Dto, Entity2> context)
            {

                Dto dto1 = context.getSource();
                Entity2 entity2= context.getDestination();
                entity2.setFullName(dto1.getFirstName() + " " + dto1.getLastName());
                entity2.setEmail(dto1.getEmail());
                return entity2;
            }
        };

        model.addConverter(myConverter);
        model.addConverter(myRevConverter);

        convertEntityToDto(model);
        convertDtoToEntity(model);

    }

    private static void convertEntityToDto(ModelMapper model) {
        Entity2 entity2 = new Entity2();
        entity2.setEmail("Santhosh.challa@gmail.com");
        entity2.setFullName("Santhosh Challa");

        Dto dto2 = new Dto();

        model.map(entity2, dto2);

        System.out.println("Entity2 values: " + entity2);
        System.out.println("DTO values: " + dto2);
    }

    private static void convertDtoToEntity(ModelMapper model) {
        Entity2 entity2 = new Entity2();

        Dto dto2 = new Dto();
        dto2.setFirstName("santhosh");
        dto2.setLastName("challa");
        dto2.setEmail("skc@gmail.com");

        model.map(dto2, entity2);

        System.out.println("DTO values: " + dto2);
        System.out.println("Entity2 values: " + entity2);

    }
}
