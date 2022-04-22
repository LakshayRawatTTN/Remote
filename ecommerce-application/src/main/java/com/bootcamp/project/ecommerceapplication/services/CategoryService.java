package com.bootcamp.project.ecommerceapplication.services;

import com.bootcamp.project.ecommerceapplication.domain.category.Category;
import com.bootcamp.project.ecommerceapplication.domain.category.CategoryMetadataField;
import com.bootcamp.project.ecommerceapplication.exceptions.FieldExist;
import com.bootcamp.project.ecommerceapplication.models.CategoryMetadataValueModel;
import com.bootcamp.project.ecommerceapplication.models.CategoryModel;
import com.bootcamp.project.ecommerceapplication.repositories.CategoryMetaDataFieldValueRepository;
import com.bootcamp.project.ecommerceapplication.repositories.CategoryMetadataFieldRepository;
import com.bootcamp.project.ecommerceapplication.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private CategoryMetadataFieldRepository categoryMetadataFieldRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMetaDataFieldValueRepository categoryMetaDataFieldValueRepository;

    //adding metadata field
    public ResponseEntity<CategoryMetadataField> addField(String name) throws FieldExist {
        if (categoryMetadataFieldRepository.findByName(name) == null) {
            CategoryMetadataField categoryMetadataField = new CategoryMetadataField();
            categoryMetadataField.setName(name);
            categoryMetadataFieldRepository.save(categoryMetadataField);
            return new ResponseEntity<CategoryMetadataField>(categoryMetadataField, HttpStatus.OK);
        } else {
            throw new FieldExist("MetaData field already Exist " + name);
        }
    }

    //getting all list
    public List<CategoryMetadataField> getMetaDataFields() {
        return categoryMetadataFieldRepository.findAll();
    }

    //adding category
    public ResponseEntity<Category> addCategory(String name) throws FieldExist {
        if (categoryRepository.findByName(name) == null) {
            Category category = new Category();
            category.setName(name);
            categoryRepository.save(category);
            return new ResponseEntity<Category>(category, HttpStatus.OK);
        } else {
            throw new FieldExist("Category already exist With name :- " + name);
        }
    }

    //adding category with parent id
    public Category addCategory(String name, long id) throws FieldExist {

        if (categoryRepository.findById(id) == null || categoryRepository.findById(id).getParentId() != 0)
            throw new FieldExist("Parent Category does not exist with id " + id);
        if (categoryRepository.findByName(name) == null && categoryRepository.findById(id).getParentId() == 0) {
            Category category = new Category();
            category.setName(name);
            category.setParentId(id);
            categoryRepository.save(category);
            return category;
        } else {
            throw new FieldExist("Category already exist with name :- " + name);
        }
    }

//view category with parent

    public Map<String, List<Category>> viewCategoryById(long id) throws FieldExist {
        if (categoryRepository.findById(id) == null)
            throw new FieldExist("Category does not exist with id " + id);
        Map<String, List<Category>> map = new HashMap<>();
        Category category = categoryRepository.findById(id);
        if (category.getParentId() == 0) {
            map.put("Parent Category", Arrays.asList(category));
            List<Category> childCat = categoryRepository.findAllByParentId(id);
            map.put("Child Category", childCat);
        }
        if (category.getParentId() != 0) {
            Category category1 = categoryRepository.findById(category.getParentId());
            map.put("Parent Category", Arrays.asList(category1));
            List<Category> childCat = categoryRepository.findAllByParentId(category.getParentId());
            map.put("Child Category", childCat);
        }
        return map;
    }

    //adding metadata value

    @Transactional
    public CategoryMetadataValueModel addMetadataValue(long id, long mid, Set<String> value) throws FieldExist {
        if(categoryRepository.findById(id)==null){
            throw new FieldExist("Category does not exist with id " +id);
        }
        else if (categoryMetadataFieldRepository.findById(mid)==null){
            throw new FieldExist("Field does not exist with id " +mid);
        }
        if(categoryMetaDataFieldValueRepository.viewMetadataValues(id, mid)!=null){
            throw new FieldExist("Field Value Already Exist With Category ID :- "+id+" and Field ID :-"+mid);
        }
        StringBuilder sb=new StringBuilder();
        for (String name : value) {
            sb.append(name);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        categoryMetaDataFieldValueRepository.addMetadataValues(id,mid,sb.toString());
        return new CategoryMetadataValueModel(id,mid,sb.toString());
    }


    //view all category
    public List<Category> viewAllCategory() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(long id, String name) throws FieldExist {
        Category category = categoryRepository.findById(id);
        if(categoryRepository.findByName(name)!=null)
            throw new FieldExist("Category Already Exist With name :- "+name);
        if(category!=null){
            category.setName(name);
            categoryRepository.save(category);
            return category;
        }else {
            throw new FieldExist("Category does not exist with id " +id);
        }
    }
    //add meta data value

    @Transactional
    public CategoryMetadataValueModel updateMetadataValue(long id, long mid, Set<String> value) throws FieldExist {
        if(categoryRepository.findById(id)==null){
            throw new FieldExist("Category does not exist with id " +id);
        }
        if (categoryMetadataFieldRepository.findById(mid)==null){
            throw new FieldExist("Field does not exist with id " +mid);
        }
        if(categoryMetaDataFieldValueRepository.viewMetadataValues(id, mid)==null){
            throw new FieldExist("Field Value does not Exist With Category ID :- "+id+" and Field ID :-"+mid);
        }
        List<Object[]> result = categoryMetaDataFieldValueRepository.viewMetadataValues2(id, mid);
        Object[] temp = result.get(0);
        String[] str = temp[2].toString().split(",");
        for(String st: str){
            value.add(st);
        }
        StringBuilder sb=new StringBuilder();
        for (String name : value) {
            sb.append(name);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        categoryMetaDataFieldValueRepository.updateMetadataValues(id,mid,sb.toString());
        return new CategoryMetadataValueModel(id,mid,sb.toString());
    }

    public LinkedHashMap<String,List<Object>> getCategoryList() {
        LinkedHashMap<String,List<Object>> map =new LinkedHashMap<>();
        List<Category> category = categoryRepository.findAll();
        map.put("Categories",Arrays.asList(category));
        List<CategoryMetadataField> categoryMetadataFields = categoryMetadataFieldRepository.findAll();
        map.put("MetaData",Arrays.asList(categoryMetadataFields));
        List<Object> object2 = categoryMetaDataFieldValueRepository.viewAllMetadataValue();
        map.put("Values",object2);
        return map;
    }

}
