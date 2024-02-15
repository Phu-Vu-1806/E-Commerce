package com.project.shopapi.config;

import com.project.shopapi.entity.Category;
import com.project.shopapi.entity.ProductSize;
import com.project.shopapi.entity.enums.ERole;
import com.project.shopapi.entity.Role;
import com.project.shopapi.repository.CategoryRepository;
import com.project.shopapi.repository.ProductSizeRepository;
import com.project.shopapi.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ObjectCreator implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Override
    public void run(String... args) throws Exception {
        RoleCreator();
        Category();
        Size();
    }

    private void RoleCreator() {
        Arrays.stream(ERole.values()).forEach(eRole -> {
            if (!roleRepository.existsByName(eRole)) {
                roleRepository.save(new Role(eRole));
            }
        });
    }

    private void Category() {
        List<String> names = new ArrayList<>();
        names.add("Áo khoác");
        names.add("Giày");
        names.add("Balo");
        for (String name : names) {
            if (!categoryRepository.existsByName(name)) {
                categoryRepository.save(new Category(name));
            }
        }
    }

    private void Size() {
        List<Integer> sizes = new ArrayList<>();
        sizes.add(35);
        sizes.add(36);
        sizes.add(38);
        sizes.add(39);
        sizes.add(40);
        sizes.add(41);
        sizes.add(42);
        sizes.add(43);
        sizes.add(44);

        for (Integer size : sizes) {
            if (!productSizeRepository.existsBySize(size)) {
                productSizeRepository.save(new ProductSize(size));
            }
        }
    }

}
