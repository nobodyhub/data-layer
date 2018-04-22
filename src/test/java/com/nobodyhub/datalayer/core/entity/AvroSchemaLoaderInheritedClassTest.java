package com.nobodyhub.datalayer.core.entity;

import com.nobodyhub.datalayer.core.entity.cases.PrimitiveClass;
import com.nobodyhub.datalayer.core.entity.cases.SimpleEnum;
import com.nobodyhub.datalayer.core.entity.cases.within.InheritedClass;
import com.nobodyhub.datalayer.core.entity.data.AvroData;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderInheritedClassTest extends AvroSchemaLoaderPrimitiveClassTest {
    @Test
    public void testLoad() {
        avroSchemaLoader.load(
                InheritedClass.class,
                PrimitiveClass.class,
                SimpleEnum.class);
        Assert.assertEquals("{\"type\":\"record\",\"name\":\"InheritedClass\",\"namespace\":\"com.nobodyhub.datalayer.core.entity.cases.within\",\"fields\":[{\"name\":\"anotherString\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"simpleEnum\",\"type\":[\"null\",{\"type\":\"enum\",\"name\":\"SimpleEnum\",\"namespace\":\"com.nobodyhub.datalayer.core.entity.cases\",\"symbols\":[\"S1\",\"S2\",\"S3\"]}],\"default\":null},{\"name\":\"primitiveClassList\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"namespace\":\"com.nobodyhub.datalayer.core.entity.cases\",\"fields\":[{\"name\":\"aString\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"aByteBuffer\",\"type\":[\"null\",\"bytes\"],\"default\":null},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aInteger\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"along\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"aLong\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"afloat\",\"type\":[\"null\",\"float\"],\"default\":null},{\"name\":\"aFloat\",\"type\":[\"null\",\"float\"],\"default\":null},{\"name\":\"adouble\",\"type\":\"double\"},{\"name\":\"aDouble\",\"type\":[\"null\",\"double\"],\"default\":null},{\"name\":\"aboolean\",\"type\":[\"null\",\"boolean\"],\"default\":null},{\"name\":\"aBoolean\",\"type\":\"boolean\"},{\"name\":\"aBigDecimal\",\"type\":[\"null\",{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":2}],\"default\":null},{\"name\":\"aDate\",\"type\":{\"type\":\"record\",\"name\":\"Date\",\"namespace\":\"java.sql\",\"fields\":[]}},{\"name\":\"aTimeStamp\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"Timestamp\",\"namespace\":\"java.sql\",\"fields\":[]}],\"default\":null},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]},\"java-class\":\"java.util.List\"}},{\"name\":\"aString\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"aByteBuffer\",\"type\":[\"null\",\"bytes\"],\"default\":null},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aInteger\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"along\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"aLong\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"afloat\",\"type\":[\"null\",\"float\"],\"default\":null},{\"name\":\"aFloat\",\"type\":[\"null\",\"float\"],\"default\":null},{\"name\":\"adouble\",\"type\":\"double\"},{\"name\":\"aDouble\",\"type\":[\"null\",\"double\"],\"default\":null},{\"name\":\"aboolean\",\"type\":[\"null\",\"boolean\"],\"default\":null},{\"name\":\"aBoolean\",\"type\":\"boolean\"},{\"name\":\"aBigDecimal\",\"type\":[\"null\",{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":2}],\"default\":null},{\"name\":\"aDate\",\"type\":\"java.sql.Date\"},{\"name\":\"aTimeStamp\",\"type\":[\"null\",\"java.sql.Timestamp\"],\"default\":null},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]}",
                AvroData.get().getSchema(InheritedClass.class).toString());
    }
}