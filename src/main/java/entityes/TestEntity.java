package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "a_test")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {
      @Id
      private Integer id;
      private String testcol1;

}
