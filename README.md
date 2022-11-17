## Android Notes
---
### Parcelable interface

-> implies serialization and deserialization

#### Special Cases

-> for serializing booleans:
```
destination.writeInt(booleanVar == null ? 0 : (booleanVar == true ? 1 : 0));
```
-> for serializing variables that could be null
	-> write a dummy Byte (0 or 1) so when you de-serialize you know if you had null or a value
```
if (variable == null) {
    destination.writeByte((byte) 0);
} 
else {
    destination.writeByte((byte) 1);
    destination.writeInt(variable);
}
```