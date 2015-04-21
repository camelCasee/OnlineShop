package ru.ncedu.onlineshop.service.dao.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import ru.ncedu.onlineshop.entity.users.Authority;
import ru.ncedu.onlineshop.service.dao.GenericDAOImpl;

/**
 * Created by Никита on 28.12.14.
 */
@Repository("grantedAuthorityDAO")
public class GrantedAuthorityDAO extends GenericDAOImpl<GrantedAuthority> {
    private static Logger logger = LoggerFactory.getLogger(AuditItemDAO.class);

    public GrantedAuthorityDAO() {
        super();
    }

    public Authority getAdminAuthority() {
        logger.info("Start. Getting admin authority.");

        Authority authority = entityManager.createQuery("select a from Authority a where a.authority = ?1", Authority.class).setParameter(1, "admin").getSingleResult();

        logger.info("End of method. Return authority = " + authority.toString());
        return authority;
    }

    public Authority getManagerAuthority() {
        logger.info("Start. Getting manager authority.");

        Authority authority = entityManager.createQuery("select a from Authority a where a.authority = ?1", Authority.class).setParameter(1, "manager").getSingleResult();

        logger.info("End of method. manager authority = " + authority.toString());
        return authority;
    }

    public Authority getUserAuthority() {
        logger.info("Start. Getting user authority.");

        Authority authority = entityManager.createQuery("select a from Authority a where a.authority = ?1", Authority.class).setParameter(1, "user").getSingleResult();

        logger.info("End of method. Return authority = " + authority.toString());
        return authority;
    }
}
