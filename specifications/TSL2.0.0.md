<!-- Logo -->
<p align="center">
  <a href="https://jitpack.io/#TheSpawnProject/TheSpawnLanguage">
    <img src="https://raw.githubusercontent.com/TheSpawnProject/TheSpawnLanguage/master/.github/assets/logo.png" height="150" alt="TSL Logo" aria-label="TheSpawnLanguage Logo" />
  </a>
  <a href="https://jitpack.io/#TheSpawnProject/TheSpawnLanguage">
    <img src="https://raw.githubusercontent.com/TheSpawnProject/TheSpawnLanguage/master/.github/assets/specifications.png" height="150" />
  </a>
</p>

<!-- Slogan -->
<p align="center">
   The Spawn Language Specifications: <i>Version 2.0.0</i>
</p>

<!-- Badges -->
<p align="center">

  <!-- Main Badges -->
  <img src="https://raw.githubusercontent.com/TheSpawnProject/TheSpawnLanguage/master/.github/assets/main-badge.png" height="20px"/>
  <a href="https://jitpack.io/#TheSpawnProject/TheSpawnLanguage">
    <img src="https://jitpack.io/v/TheSpawnProject/TheSpawnLanguage.svg"/>
  </a>
  <a href="https://github.com/TheSpawnProject/TheSpawnLanguage/releases">
    <img src="https://img.shields.io/github/v/release/TheSpawnProject/TheSpawnLanguage"/>
  </a>
  <a href="https://github.com/TheSpawnProject/TheSpawnLanguage/releases">
    <img src="https://img.shields.io/github/v/release/TheSpawnProject/TheSpawnLanguage?include_prereleases&label=release-snapshot"/>
  </a>
  <a href="https://github.com/TheSpawnProject/TheSpawnLanguage">
    <img src="https://img.shields.io/github/languages/top/TheSpawnProject/TheSpawnLanguage"/>
  </a>

  <br/>

  <!-- Github Badges -->
  <img src="https://raw.githubusercontent.com/TheSpawnProject/TheSpawnLanguage/master/.github/assets/github-badge.png" height="20px"/>
  <a href="https://github.com/TheSpawnProject/TheSpawnLanguage/commits/master">
    <img src="https://img.shields.io/github/last-commit/TheSpawnProject/TheSpawnLanguage"/>
  </a>
  <a href="https://github.com/TheSpawnProject/TheSpawnLanguage/issues">
    <img src="https://img.shields.io/github/issues/TheSpawnProject/TheSpawnLanguage"/>
  </a>
  <a href="https://github.com/TheSpawnProject/TheSpawnLanguage/tree/master/src">
    <img src="https://img.shields.io/github/languages/code-size/TheSpawnProject/TheSpawnLanguage"/>
  </a>

  <br/>

  <!-- Support Badges -->
  <img src="https://raw.githubusercontent.com/TheSpawnProject/TheSpawnLanguage/master/.github/assets/support-badge.png" height="20px"/>
  <a href="https://discord.gg/KNxxdvN">
    <img src="https://img.shields.io/discord/610497509437210624?label=discord"/>
  </a>
  <a href="https://www.patreon.com/iGoodie">
    <img src="https://img.shields.io/endpoint.svg?url=https%3A%2F%2Fshieldsio-patreon.vercel.app%2Fapi%3Fusername%3DiGoodie%26type%3Dpatrons"/>
  </a>
</p>

# Introduction

The Spawn Language (TSL) is a ruleset format which helps users to tell a system how to behave when certain events occur.

# 1. File Format

- TSL files MUST use the extension `.tsl`
- TSL files MUST be UTF-8 encoded.

# 2. Tokens

## 2.1. Plain Word Token

Obviously, most basic token type is the Plain Word token. By default, any sequence of characters that does not fit into
other token definitions is a Plain Word Token. Plain Words cannot contain whitespaces in them.

```tsl
PRINT ALL THESE WORDS

# ^ Tokenizes into those Plain Word tokens;
# 1. PRINT
# 2. ALL
# 3. THESE
# 4. WORDS
```

## 2.2. Expression Token

Expressions are evaluative tokens that are meant to be calculated during *perform-time* using a custom *JavaScript
expression syntax*. Expression tokens begin with a dollar sign followed by a left curly bracket (`${`) and ends with a
right curly bracket (`}`). Sequence that lies between is a valid Javascript expression.

```tsl
# Turns into value 2 during perform-time
${1 + 1}

# Supports built-in JS objects like "Math", "Date" and "RegExp"
${Math.cos(Math.PI * 2)}

# Supports more complex stuff too
${let x = 10; }
```

# 3. Syntax

## 3.1. Comments

There are two kinds of TSL comments; inline comments and multi-line comments.

Inline comments start with a hash (`#`)
character and makes the interpreter ignore the content up until the next new-line character (`\r?\n`) or
end-of-file (`eof`).

```tsl
# This is an inline comment.
PRINT %Hello World!% # This is also a valid inline comment.
# This too!
ON Program Initialize
```

Multi-line comments start with a hash followed by an asterisk character (`#*`) and makes the interpreter ignore the
content up until the next asterisk character followed by a hash character (`*#`).

# Yet to be continued...

## License

&copy; 2021 Taha Anılcan Metinyurt (iGoodie)

For any part of this work for which the license is applicable, this work is licensed under
the [Attribution-NonCommercial-NoDerivatives 4.0 International](http://creativecommons.org/licenses/by-nc-nd/4.0/)
license. (See LICENSE).

<a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-nd/4.0/88x31.png" /></a>