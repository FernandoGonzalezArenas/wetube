package com.wetube.admin.entity;

public enum PredefinedReason {
SPAM("Spam o contenido engañoso"),
    VIOLENCIA("Contenido violento o explícito"),
    ACOSO("Acoso o ciberbullying"),
    DERECHOS_AUTOR("Infraccion por derechos de autor"),
    OTROS("Otros motivos");

private final String description;

PredefinedReason(String description){
    this.description=description;
}

public String getDescription(){
    return description;
}

}
