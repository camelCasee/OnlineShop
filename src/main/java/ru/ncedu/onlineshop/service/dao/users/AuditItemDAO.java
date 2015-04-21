package ru.ncedu.onlineshop.service.dao.users;

import org.springframework.stereotype.Repository;
import ru.ncedu.onlineshop.entity.users.AuditItem;
import ru.ncedu.onlineshop.service.dao.GenericDAOImpl;

/**
 * Created by Никита on 28.12.14.
 */
@Repository("auditItemDAO")
public class AuditItemDAO extends GenericDAOImpl<AuditItem> {

    public AuditItemDAO() {
        super();
    }

}
