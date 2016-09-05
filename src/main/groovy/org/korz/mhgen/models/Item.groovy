package org.korz.mhgen.models

class Item {
    String name
    int rarity

    Item(row) {
        this.name = row.name
        this.rarity = row.rarity
    }
}
