# business-rule-engine
A simple business rule engine

Business Rule = Condition + Action

Usually the business conditions are written in Excel / CSV

### Feature
- Handle business rule conditions as CSV / JSON
- Business rule conditions can be stored as files or into database
- Only one database table to store different kinds of conditions
- No complex configurations

### Conditions
- Conditions are stored as an array of objects in JSON
- Multiple JSON can be stored in files / one single database table, each JSON as a CLOB of one record
- JSON can be converted as CSV for business to edit - to be implemented
- Versioning for the JSON - to be implemented

### Rule
- Rule combines conditions and action (java method)

### "Fact"
- A "Fact" is a runtime object instance to be checked against / applied on the rule
