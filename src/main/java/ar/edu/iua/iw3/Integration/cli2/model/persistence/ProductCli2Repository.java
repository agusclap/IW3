package ar.edu.iua.iw3.integration.cli2.model.persistence;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimView;

@Repository
public interface ProductCli2Repository extends JpaRepository<ProductCli2, Long> {
    public List<ProductCli2> findByExpirationDateBeforeOrderByExpirationDateDesc(Date expirationDate);

    public List<ProductCli2SlimView> findByOrderByPrecioDesc();

    public List<ProductCli2> findByPrecioBetweenOrderByPrecioAsc(double startPrice, double endPrice);

    public List<ProductCli2> findByPrecioGreaterThanEqualOrderByPrecioAsc(double startPrice);

    public List<ProductCli2> findByPrecioLessThanEqualOrderByPrecioAsc(double endPrice);

    public List<ProductCli2> findAllByOrderByPrecioAsc();
}
