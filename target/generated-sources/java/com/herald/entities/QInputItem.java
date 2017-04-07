package com.herald.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QInputItem is a Querydsl query type for InputItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInputItem extends EntityPathBase<InputItem> {

    private static final long serialVersionUID = 646196621L;

    public static final QInputItem inputItem = new QInputItem("inputItem");

    public final StringPath CTLG_ID = createString("CTLG_ID");

    public final StringPath CTLG_IMG_URL = createString("CTLG_IMG_URL");

    public final StringPath CTLG_NM = createString("CTLG_NM");

    public final StringPath CTLG_TAG = createString("CTLG_TAG");

    public final StringPath DCTGR_ID = createString("DCTGR_ID");

    public final StringPath DCTGR_NM = createString("DCTGR_NM");

    public final StringPath LCTGR_ID = createString("LCTGR_ID");

    public final StringPath LCTGR_NM = createString("LCTGR_NM");

    public final StringPath MCTGR_ID = createString("MCTGR_ID");

    public final StringPath MCTGR_NM = createString("MCTGR_NM");

    public final StringPath PRD_ID = createString("PRD_ID");

    public final StringPath PRD_IMG_URL = createString("PRD_IMG_URL");

    public final StringPath PRD_NM = createString("PRD_NM");

    public final StringPath SCTGR_ID = createString("SCTGR_ID");

    public final StringPath SCTGR_NM = createString("SCTGR_NM");

    public QInputItem(String variable) {
        super(InputItem.class, forVariable(variable));
    }

    public QInputItem(Path<? extends InputItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInputItem(PathMetadata metadata) {
        super(InputItem.class, metadata);
    }

}

