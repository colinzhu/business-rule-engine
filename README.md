# CSV Business Rules Engine
A design of business rule engine which handles business rules in CSV

Business Rule = Condition + Action

Usually the business conditions are written in Excel / CSV

### Feature
- Handle business rules as CSV / JSON
- Zero or only one database table 
- No complex configuration files
- One java class for one rule

### Business rule config
- Usually the business rules are stored as a table in Excel / CSV.
  This CSV is treated as a **business rule config**
  When parsing the CSV, one record can be read as: 
  > when (columnA = x and columnB = y) then use columnC
- CSV format can be converted to JSON for application to process and store
- JSON will be easy for UI to render as a table for user to edit
- JSON can be persisted as files or in database
- If stored in database, different kinds of JSON will be store in one single table
- By saving different versions of JSON into database, the application can support versioning

### Rule
- A Rule is a single java class which parses the business rule config to define the conditions and actions
- So Rule = Business rule config + Java logic

### "Fact"
- A "Fact" is a runtime object instance to be checked against / applied on a rule
- To prevent creating different java classes for different types of fact, Jackson library's JsonNode is used as the type for fact

### RuleEngine
- A component combines Rule and Fact, and kick off the processing.

### Result
- A result contains the rule's name, processing result, and a remark
- For actions without any return value, processing result can be null or String e.g. DONE or anything else

### Reference
- Martin Fowler's [RulesEngine](https://martinfowler.com/bliki/RulesEngine.html)
- [Easy Rules](https://github.com/j-easy/easy-rules) 