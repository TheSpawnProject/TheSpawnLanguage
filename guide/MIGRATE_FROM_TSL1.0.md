# 🔼 Migrate from TSL1.0 to TSL1.5

## 1. 🛠 Predicate Property Alias Deprecation

*Predicate Property* aliases are deprecated. Use the base names instead. Example:

```diff
DROP apple
 ON Donation
- WITH donation_currency IS USD
+ WITH currency IS USD
```

### Full change list:

- ❌ `donation_currency` -> `currency` ✔
- ❌ `donation_amount` -> `amount` ✔
- ❌ `subscription_months` -> `months` ✔
- ❌ `subscription_tier` -> `tier` ✔
- ❌ `viewer_count` -> `viewers` ✔
- ❌ `raider_count` -> `raiders` ✔
- ❌ `chat_badges` -> `badges` ✔