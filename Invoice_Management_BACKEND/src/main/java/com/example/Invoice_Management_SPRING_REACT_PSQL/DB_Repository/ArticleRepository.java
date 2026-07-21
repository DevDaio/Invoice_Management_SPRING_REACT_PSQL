package com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.Article;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

  	List<Article> findBySupplierId(int supplierId);

   	List<Article> findByInvoiceId(int invoiceId);

    List<Article> findByName(String name);

    List<Article> findByArticleNumber(int articleNumber);

}