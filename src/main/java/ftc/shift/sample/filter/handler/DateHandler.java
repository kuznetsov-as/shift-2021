package ftc.shift.sample.filter.handler;

import ftc.shift.sample.filter.SearchCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Date;

public class DateHandler implements Handler {
    @Override
    public <T> Predicate handle(SearchCriteria criteria, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(criteria.getType() == null || !criteria.getType().equals("date")) return null;
        try {
            Date date = Date.valueOf(criteria.getValue().toString());
            switch (criteria.getOperation()) {
                case ">":
                    return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), date);
                case "<":
                    return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), date);
                case "=":
                    return criteriaBuilder.equal(root.get(criteria.getKey()), date);
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
        return null;
    }
}
