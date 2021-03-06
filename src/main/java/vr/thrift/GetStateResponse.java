/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package vr.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2014-12-12")
public class GetStateResponse implements org.apache.thrift.TBase<GetStateResponse, GetStateResponse._Fields>, java.io.Serializable, Cloneable, Comparable<GetStateResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("GetStateResponse");

  private static final org.apache.thrift.protocol.TField VIEW_NUMBER_FIELD_DESC = new org.apache.thrift.protocol.TField("viewNumber", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField LOGS_FIELD_DESC = new org.apache.thrift.protocol.TField("logs", org.apache.thrift.protocol.TType.MAP, (short)2);
  private static final org.apache.thrift.protocol.TField CHECK_POINT_FIELD_DESC = new org.apache.thrift.protocol.TField("checkPoint", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField OP_NUMBER_FIELD_DESC = new org.apache.thrift.protocol.TField("opNumber", org.apache.thrift.protocol.TType.I32, (short)4);
  private static final org.apache.thrift.protocol.TField COMMIT_NUMBER_FIELD_DESC = new org.apache.thrift.protocol.TField("commitNumber", org.apache.thrift.protocol.TType.I32, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new GetStateResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new GetStateResponseTupleSchemeFactory());
  }

  public int viewNumber; // required
  public Map<Integer,Log> logs; // required
  public int checkPoint; // required
  public int opNumber; // required
  public int commitNumber; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VIEW_NUMBER((short)1, "viewNumber"),
    LOGS((short)2, "logs"),
    CHECK_POINT((short)3, "checkPoint"),
    OP_NUMBER((short)4, "opNumber"),
    COMMIT_NUMBER((short)5, "commitNumber");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // VIEW_NUMBER
          return VIEW_NUMBER;
        case 2: // LOGS
          return LOGS;
        case 3: // CHECK_POINT
          return CHECK_POINT;
        case 4: // OP_NUMBER
          return OP_NUMBER;
        case 5: // COMMIT_NUMBER
          return COMMIT_NUMBER;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __VIEWNUMBER_ISSET_ID = 0;
  private static final int __CHECKPOINT_ISSET_ID = 1;
  private static final int __OPNUMBER_ISSET_ID = 2;
  private static final int __COMMITNUMBER_ISSET_ID = 3;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VIEW_NUMBER, new org.apache.thrift.meta_data.FieldMetaData("viewNumber", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    tmpMap.put(_Fields.LOGS, new org.apache.thrift.meta_data.FieldMetaData("logs", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32            , "int"), 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Log.class))));
    tmpMap.put(_Fields.CHECK_POINT, new org.apache.thrift.meta_data.FieldMetaData("checkPoint", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    tmpMap.put(_Fields.OP_NUMBER, new org.apache.thrift.meta_data.FieldMetaData("opNumber", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    tmpMap.put(_Fields.COMMIT_NUMBER, new org.apache.thrift.meta_data.FieldMetaData("commitNumber", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(GetStateResponse.class, metaDataMap);
  }

  public GetStateResponse() {
  }

  public GetStateResponse(
    int viewNumber,
    Map<Integer,Log> logs,
    int checkPoint,
    int opNumber,
    int commitNumber)
  {
    this();
    this.viewNumber = viewNumber;
    setViewNumberIsSet(true);
    this.logs = logs;
    this.checkPoint = checkPoint;
    setCheckPointIsSet(true);
    this.opNumber = opNumber;
    setOpNumberIsSet(true);
    this.commitNumber = commitNumber;
    setCommitNumberIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public GetStateResponse(GetStateResponse other) {
    __isset_bitfield = other.__isset_bitfield;
    this.viewNumber = other.viewNumber;
    if (other.isSetLogs()) {
      Map<Integer,Log> __this__logs = new HashMap<Integer,Log>(other.logs.size());
      for (Map.Entry<Integer, Log> other_element : other.logs.entrySet()) {

        Integer other_element_key = other_element.getKey();
        Log other_element_value = other_element.getValue();

        Integer __this__logs_copy_key = other_element_key;

        Log __this__logs_copy_value = new Log(other_element_value);

        __this__logs.put(__this__logs_copy_key, __this__logs_copy_value);
      }
      this.logs = __this__logs;
    }
    this.checkPoint = other.checkPoint;
    this.opNumber = other.opNumber;
    this.commitNumber = other.commitNumber;
  }

  public GetStateResponse deepCopy() {
    return new GetStateResponse(this);
  }

  @Override
  public void clear() {
    setViewNumberIsSet(false);
    this.viewNumber = 0;
    this.logs = null;
    setCheckPointIsSet(false);
    this.checkPoint = 0;
    setOpNumberIsSet(false);
    this.opNumber = 0;
    setCommitNumberIsSet(false);
    this.commitNumber = 0;
  }

  public int getViewNumber() {
    return this.viewNumber;
  }

  public GetStateResponse setViewNumber(int viewNumber) {
    this.viewNumber = viewNumber;
    setViewNumberIsSet(true);
    return this;
  }

  public void unsetViewNumber() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __VIEWNUMBER_ISSET_ID);
  }

  /** Returns true if field viewNumber is set (has been assigned a value) and false otherwise */
  public boolean isSetViewNumber() {
    return EncodingUtils.testBit(__isset_bitfield, __VIEWNUMBER_ISSET_ID);
  }

  public void setViewNumberIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __VIEWNUMBER_ISSET_ID, value);
  }

  public int getLogsSize() {
    return (this.logs == null) ? 0 : this.logs.size();
  }

  public void putToLogs(int key, Log val) {
    if (this.logs == null) {
      this.logs = new HashMap<Integer,Log>();
    }
    this.logs.put(key, val);
  }

  public Map<Integer,Log> getLogs() {
    return this.logs;
  }

  public GetStateResponse setLogs(Map<Integer,Log> logs) {
    this.logs = logs;
    return this;
  }

  public void unsetLogs() {
    this.logs = null;
  }

  /** Returns true if field logs is set (has been assigned a value) and false otherwise */
  public boolean isSetLogs() {
    return this.logs != null;
  }

  public void setLogsIsSet(boolean value) {
    if (!value) {
      this.logs = null;
    }
  }

  public int getCheckPoint() {
    return this.checkPoint;
  }

  public GetStateResponse setCheckPoint(int checkPoint) {
    this.checkPoint = checkPoint;
    setCheckPointIsSet(true);
    return this;
  }

  public void unsetCheckPoint() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __CHECKPOINT_ISSET_ID);
  }

  /** Returns true if field checkPoint is set (has been assigned a value) and false otherwise */
  public boolean isSetCheckPoint() {
    return EncodingUtils.testBit(__isset_bitfield, __CHECKPOINT_ISSET_ID);
  }

  public void setCheckPointIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __CHECKPOINT_ISSET_ID, value);
  }

  public int getOpNumber() {
    return this.opNumber;
  }

  public GetStateResponse setOpNumber(int opNumber) {
    this.opNumber = opNumber;
    setOpNumberIsSet(true);
    return this;
  }

  public void unsetOpNumber() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __OPNUMBER_ISSET_ID);
  }

  /** Returns true if field opNumber is set (has been assigned a value) and false otherwise */
  public boolean isSetOpNumber() {
    return EncodingUtils.testBit(__isset_bitfield, __OPNUMBER_ISSET_ID);
  }

  public void setOpNumberIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __OPNUMBER_ISSET_ID, value);
  }

  public int getCommitNumber() {
    return this.commitNumber;
  }

  public GetStateResponse setCommitNumber(int commitNumber) {
    this.commitNumber = commitNumber;
    setCommitNumberIsSet(true);
    return this;
  }

  public void unsetCommitNumber() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __COMMITNUMBER_ISSET_ID);
  }

  /** Returns true if field commitNumber is set (has been assigned a value) and false otherwise */
  public boolean isSetCommitNumber() {
    return EncodingUtils.testBit(__isset_bitfield, __COMMITNUMBER_ISSET_ID);
  }

  public void setCommitNumberIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __COMMITNUMBER_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case VIEW_NUMBER:
      if (value == null) {
        unsetViewNumber();
      } else {
        setViewNumber((Integer)value);
      }
      break;

    case LOGS:
      if (value == null) {
        unsetLogs();
      } else {
        setLogs((Map<Integer,Log>)value);
      }
      break;

    case CHECK_POINT:
      if (value == null) {
        unsetCheckPoint();
      } else {
        setCheckPoint((Integer)value);
      }
      break;

    case OP_NUMBER:
      if (value == null) {
        unsetOpNumber();
      } else {
        setOpNumber((Integer)value);
      }
      break;

    case COMMIT_NUMBER:
      if (value == null) {
        unsetCommitNumber();
      } else {
        setCommitNumber((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case VIEW_NUMBER:
      return Integer.valueOf(getViewNumber());

    case LOGS:
      return getLogs();

    case CHECK_POINT:
      return Integer.valueOf(getCheckPoint());

    case OP_NUMBER:
      return Integer.valueOf(getOpNumber());

    case COMMIT_NUMBER:
      return Integer.valueOf(getCommitNumber());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case VIEW_NUMBER:
      return isSetViewNumber();
    case LOGS:
      return isSetLogs();
    case CHECK_POINT:
      return isSetCheckPoint();
    case OP_NUMBER:
      return isSetOpNumber();
    case COMMIT_NUMBER:
      return isSetCommitNumber();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof GetStateResponse)
      return this.equals((GetStateResponse)that);
    return false;
  }

  public boolean equals(GetStateResponse that) {
    if (that == null)
      return false;

    boolean this_present_viewNumber = true;
    boolean that_present_viewNumber = true;
    if (this_present_viewNumber || that_present_viewNumber) {
      if (!(this_present_viewNumber && that_present_viewNumber))
        return false;
      if (this.viewNumber != that.viewNumber)
        return false;
    }

    boolean this_present_logs = true && this.isSetLogs();
    boolean that_present_logs = true && that.isSetLogs();
    if (this_present_logs || that_present_logs) {
      if (!(this_present_logs && that_present_logs))
        return false;
      if (!this.logs.equals(that.logs))
        return false;
    }

    boolean this_present_checkPoint = true;
    boolean that_present_checkPoint = true;
    if (this_present_checkPoint || that_present_checkPoint) {
      if (!(this_present_checkPoint && that_present_checkPoint))
        return false;
      if (this.checkPoint != that.checkPoint)
        return false;
    }

    boolean this_present_opNumber = true;
    boolean that_present_opNumber = true;
    if (this_present_opNumber || that_present_opNumber) {
      if (!(this_present_opNumber && that_present_opNumber))
        return false;
      if (this.opNumber != that.opNumber)
        return false;
    }

    boolean this_present_commitNumber = true;
    boolean that_present_commitNumber = true;
    if (this_present_commitNumber || that_present_commitNumber) {
      if (!(this_present_commitNumber && that_present_commitNumber))
        return false;
      if (this.commitNumber != that.commitNumber)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_viewNumber = true;
    list.add(present_viewNumber);
    if (present_viewNumber)
      list.add(viewNumber);

    boolean present_logs = true && (isSetLogs());
    list.add(present_logs);
    if (present_logs)
      list.add(logs);

    boolean present_checkPoint = true;
    list.add(present_checkPoint);
    if (present_checkPoint)
      list.add(checkPoint);

    boolean present_opNumber = true;
    list.add(present_opNumber);
    if (present_opNumber)
      list.add(opNumber);

    boolean present_commitNumber = true;
    list.add(present_commitNumber);
    if (present_commitNumber)
      list.add(commitNumber);

    return list.hashCode();
  }

  @Override
  public int compareTo(GetStateResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetViewNumber()).compareTo(other.isSetViewNumber());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetViewNumber()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.viewNumber, other.viewNumber);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetLogs()).compareTo(other.isSetLogs());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLogs()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.logs, other.logs);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCheckPoint()).compareTo(other.isSetCheckPoint());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCheckPoint()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.checkPoint, other.checkPoint);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetOpNumber()).compareTo(other.isSetOpNumber());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOpNumber()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.opNumber, other.opNumber);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCommitNumber()).compareTo(other.isSetCommitNumber());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCommitNumber()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.commitNumber, other.commitNumber);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("GetStateResponse(");
    boolean first = true;

    sb.append("viewNumber:");
    sb.append(this.viewNumber);
    first = false;
    if (!first) sb.append(", ");
    sb.append("logs:");
    if (this.logs == null) {
      sb.append("null");
    } else {
      sb.append(this.logs);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("checkPoint:");
    sb.append(this.checkPoint);
    first = false;
    if (!first) sb.append(", ");
    sb.append("opNumber:");
    sb.append(this.opNumber);
    first = false;
    if (!first) sb.append(", ");
    sb.append("commitNumber:");
    sb.append(this.commitNumber);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class GetStateResponseStandardSchemeFactory implements SchemeFactory {
    public GetStateResponseStandardScheme getScheme() {
      return new GetStateResponseStandardScheme();
    }
  }

  private static class GetStateResponseStandardScheme extends StandardScheme<GetStateResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, GetStateResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VIEW_NUMBER
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.viewNumber = iprot.readI32();
              struct.setViewNumberIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // LOGS
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map38 = iprot.readMapBegin();
                struct.logs = new HashMap<Integer,Log>(2*_map38.size);
                int _key39;
                Log _val40;
                for (int _i41 = 0; _i41 < _map38.size; ++_i41)
                {
                  _key39 = iprot.readI32();
                  _val40 = new Log();
                  _val40.read(iprot);
                  struct.logs.put(_key39, _val40);
                }
                iprot.readMapEnd();
              }
              struct.setLogsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // CHECK_POINT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.checkPoint = iprot.readI32();
              struct.setCheckPointIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // OP_NUMBER
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.opNumber = iprot.readI32();
              struct.setOpNumberIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // COMMIT_NUMBER
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.commitNumber = iprot.readI32();
              struct.setCommitNumberIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, GetStateResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(VIEW_NUMBER_FIELD_DESC);
      oprot.writeI32(struct.viewNumber);
      oprot.writeFieldEnd();
      if (struct.logs != null) {
        oprot.writeFieldBegin(LOGS_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.I32, org.apache.thrift.protocol.TType.STRUCT, struct.logs.size()));
          for (Map.Entry<Integer, Log> _iter42 : struct.logs.entrySet())
          {
            oprot.writeI32(_iter42.getKey());
            _iter42.getValue().write(oprot);
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(CHECK_POINT_FIELD_DESC);
      oprot.writeI32(struct.checkPoint);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(OP_NUMBER_FIELD_DESC);
      oprot.writeI32(struct.opNumber);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(COMMIT_NUMBER_FIELD_DESC);
      oprot.writeI32(struct.commitNumber);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class GetStateResponseTupleSchemeFactory implements SchemeFactory {
    public GetStateResponseTupleScheme getScheme() {
      return new GetStateResponseTupleScheme();
    }
  }

  private static class GetStateResponseTupleScheme extends TupleScheme<GetStateResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, GetStateResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetViewNumber()) {
        optionals.set(0);
      }
      if (struct.isSetLogs()) {
        optionals.set(1);
      }
      if (struct.isSetCheckPoint()) {
        optionals.set(2);
      }
      if (struct.isSetOpNumber()) {
        optionals.set(3);
      }
      if (struct.isSetCommitNumber()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetViewNumber()) {
        oprot.writeI32(struct.viewNumber);
      }
      if (struct.isSetLogs()) {
        {
          oprot.writeI32(struct.logs.size());
          for (Map.Entry<Integer, Log> _iter43 : struct.logs.entrySet())
          {
            oprot.writeI32(_iter43.getKey());
            _iter43.getValue().write(oprot);
          }
        }
      }
      if (struct.isSetCheckPoint()) {
        oprot.writeI32(struct.checkPoint);
      }
      if (struct.isSetOpNumber()) {
        oprot.writeI32(struct.opNumber);
      }
      if (struct.isSetCommitNumber()) {
        oprot.writeI32(struct.commitNumber);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, GetStateResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.viewNumber = iprot.readI32();
        struct.setViewNumberIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TMap _map44 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.I32, org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.logs = new HashMap<Integer,Log>(2*_map44.size);
          int _key45;
          Log _val46;
          for (int _i47 = 0; _i47 < _map44.size; ++_i47)
          {
            _key45 = iprot.readI32();
            _val46 = new Log();
            _val46.read(iprot);
            struct.logs.put(_key45, _val46);
          }
        }
        struct.setLogsIsSet(true);
      }
      if (incoming.get(2)) {
        struct.checkPoint = iprot.readI32();
        struct.setCheckPointIsSet(true);
      }
      if (incoming.get(3)) {
        struct.opNumber = iprot.readI32();
        struct.setOpNumberIsSet(true);
      }
      if (incoming.get(4)) {
        struct.commitNumber = iprot.readI32();
        struct.setCommitNumberIsSet(true);
      }
    }
  }

}

