package com.herald.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QResultItem is a Querydsl query type for ResultItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QResultItem extends EntityPathBase<ResultItem> {

    private static final long serialVersionUID = 913700672L;

    public static final QResultItem resultItem = new QResultItem("resultItem");

    public final StringPath CLABEL = createString("CLABEL");

    public final StringPath CLABEL_NNDID = createString("CLABEL_NNDID");

    public final StringPath FEAT = createString("FEAT");

    public final StringPath LCTGR_NM = createString("LCTGR_NM");

    public final StringPath MCTGR_NM = createString("MCTGR_NM");

    public final StringPath PRD_ID = createString("PRD_ID");

    public final StringPath PRD_IMG_URL = createString("PRD_IMG_URL");

    public final StringPath PRD_NM = createString("PRD_NM");

    public final StringPath SCTGR_NM = createString("SCTGR_NM");

    public QResultItem(String variable) {
        super(ResultItem.class, forVariable(variable));
    }

    public QResultItem(Path<? extends ResultItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QResultItem(PathMetadata metadata) {
        super(ResultItem.class, metadata);
    }

}

