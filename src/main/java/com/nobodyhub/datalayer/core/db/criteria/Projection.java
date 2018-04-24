package com.nobodyhub.datalayer.core.db.criteria;

import com.google.common.base.Strings;
import com.nobodyhub.datalayer.core.exception.AvroCoreException;
import lombok.Data;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

/**
 * @author yan_h
 * @since 2018-04-24.
 */
@Data
public class Projection {
    /**
     * The type of projections
     */
    private ProjectionType type;
    /**
     * the name of filed to be used in projection
     */
    private String fidldName;
    /**
     * the alias to used for the projection,
     * if no, leave null
     */
    private String alias;

    public void addToProjectionList(ProjectionList projectionList) {
        org.hibernate.criterion.Projection proj = null;
        switch (type) {
            case GROUP_PROPERTY: {
                proj = Projections.groupProperty(fidldName);
                break;
            }
            case ROW_COUNT: {
                proj = Projections.rowCount();
                break;
            }
            case MAX: {
                proj = Projections.max(fidldName);
                break;
            }
            default: {
                //TODO: add all types
                throw new AvroCoreException("Not Implemented!");
            }
        }
        if (Strings.isNullOrEmpty(alias)) {
            projectionList.add(proj);
        } else {
            projectionList.add(proj, alias);
        }
    }
}
