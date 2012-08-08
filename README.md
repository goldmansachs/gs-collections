# Introduction

Ponzu is a fork of the Goldman Sachs gs-collections framework.  The main difference is that the primary iteration protocol methods have been renamed:

| **gs-collections** |  **ponzu**     |
|----------------|------------| 
| select         | filter     |
| reject         | filterNot  |
| detect         | find       |
| collect        | transform  |
| injectInto     | foldLeft   |

In addition, the following have also been renamed:

| **gs-collections** |  **ponzu**     |
|----------------|------------| 
| Function0         | Generator     |
| Functions0        | Generators    |
| PassThruFunction0 | Constant      |
| CheckedFunction0  | CheckedGenerator |

# Usage

To use this in your own project, add the following dependencies:

    <dependency>
     <groupId>com.webguys.ponzu</groupId>
     <artifactId>api</artifactId>
     <version>1.2.0</version>
    </dependency>
    
    <dependency>
     <groupId>com.webguys.ponzu</groupId>
     <artifactId>impl</artifactId>
     <version>1.2.0</version>
    </dependency>
    
    If you would like to use the test utilities, add:
    <dependency>
     <groupId>com.webguys.ponzu</groupId>
     <artifactId>testutils</artifactId>
     <version>1.2.0</version>
     <scope>test</scope>
    </dependency>

# Licensing

Please see the file called LICENSE-2.0.txt

Licensed under the Apache License, Version 2.0 (the "License");
ou may not use this file except in compliance with the License.

You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
