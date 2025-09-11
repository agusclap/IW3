package ar.edu.iua.iw3.model.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.iw3.model.Product;
import ar.edu.iua.iw3.model.persistence.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Service // esto se instancia por cada IProduct
@Slf4j
public class ProductBusiness implements IProductBusiness {

    @Autowired
    private ProductRepository productDAO; // Patron data access object Repositorio Spring

    @Override
    public List<Product> list() throws BusinessException {
        try{
            return productDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).message(e.getMessage()).build();
        }
    }

    @Override
    public Product load(long id) throws NotFoundException, BusinessException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }

    @Override
    public Product load(String product) throws NotFoundException, BusinessException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }

    @Override
    public Product add(Product product) throws FoundException, BusinessException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public Product update(Product product) throws NotFoundException, BusinessException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(long id) throws NotFoundException, BusinessException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
