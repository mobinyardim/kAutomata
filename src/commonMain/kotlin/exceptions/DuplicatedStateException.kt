package exceptions

class DuplicatedStateException(stateName: String) : Exception("$stateName already exist!")