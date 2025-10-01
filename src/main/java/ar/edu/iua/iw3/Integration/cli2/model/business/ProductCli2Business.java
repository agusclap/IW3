package ar.edu.iua.iw3.integration.cli2.model.business;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimView;
import ar.edu.iua.iw3.integration.cli2.model.persistence.ProductCli2Repository;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.IProductBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductCli2Business implements IProductCli2Business {

    @Autowired(required = false)
    private ProductCli2Repository productDAO;

    @Autowired
    private IProductBusiness productBaseBusiness;

    @Override
    public List<ProductCli2> listExpired(Date date) throws BusinessException {
        try {
            return productDAO.findByExpirationDateBeforeOrderByExpirationDateDesc(date);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();      
        }
    }
    
    @Override
    public List<ProductCli2SlimView> listSlim() throws BusinessException {
        try {
            return productDAO.findByOrderByPrecioDesc();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public List<ProductCli2> listByPrice(Double startPrice, Double endPrice) throws BusinessException {
        try {
            if (startPrice != null && endPrice != null) {
                double lower = Math.min(startPrice, endPrice);
                double upper = Math.max(startPrice, endPrice);
                return productDAO.findByPrecioBetweenOrderByPrecioAsc(lower, upper);
            }

            if (startPrice != null) {
                return productDAO.findByPrecioGreaterThanEqualOrderByPrecioAsc(startPrice);
            }

            if (endPrice != null) {
                return productDAO.findByPrecioLessThanEqualOrderByPrecioAsc(endPrice);
            }

            return productDAO.findAllByOrderByPrecioAsc();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public ProductCli2 add(ProductCli2 product) throws FoundException, BusinessException {
        try {
            productBaseBusiness.load(product.getId());
            throw FoundException.builder().message("Se encontró el Producto id=" + product.getId()).build();
        } catch (NotFoundException e) {
        }

        try {
            productBaseBusiness.load(product.getProduct());
            throw FoundException.builder().message("Se encontró el Producto '" + product.getProduct() + "'").build();
        } catch (NotFoundException e) {
        }

        try {
            return productDAO.save(product);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public ProductCli2 addExternal(ProductCli2 product) throws FoundException, BusinessException {
        return add(product);
    }
}
