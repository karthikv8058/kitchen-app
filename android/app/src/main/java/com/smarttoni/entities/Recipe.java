package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.models.Locales;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class Recipe{

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_INGREDIENT = 2;

    public static final int STATUS_PUBLISHED = 2;

    public static final int INVENTORY_MANAGED = 1;
    public static final int INVENTORY_NO_STOCK = 2;
    public static final int INVENTORY_INFINITY = 3;

    @Transient
    private long duration;

    @SerializedName("uuid")
    @Id
    private String id;

    private String name;

    @SerializedName("printerName")
    private String printerName;

    private String description;

    @SerializedName("outputQuantity")
    private float outputQuantity;

    @SerializedName("color")
    private String color;

    @SerializedName("outputUnit")
    private String outputUnitId;

    @Transient
    private Units outputUnits;

    private String roomId;

    @SerializedName("storage_id")
    private String storageId;

    @SerializedName("rack_id")
    private String rackId;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("image")
    private String imageUrl;

    private Date createdAt;

    private Date updatedAt;

    @SerializedName("recipe_category_uuid")
    private String recipeCategoryId;

    @SerializedName("lookup")
    private String lookUp;

    @SerializedName("type")
    private int type;

    @SerializedName("status")
    private int status;

    @Property(nameInDb = "version")
    @SerializedName("version")
    private int version;

    @Property(nameInDb = "productBarcode")
    @SerializedName("productBarcode")
    private String productBarcode;

    @Property(nameInDb = "recipeLabels")
    @SerializedName("recipeLabels")
    private String parentLabel;

    @SerializedName("recipeIngredients")
    private String taskIngredientComaSperatad;

    private String supplier;

    private String supplierName;

    private int inventoryType;

    public String getTaskIngredientComaSperatad() {
        return taskIngredientComaSperatad;
    }

    public void setTaskIngredientComaSperatad(String taskIngredientComaSperatad) {
        this.taskIngredientComaSperatad = taskIngredientComaSperatad;
    }

    @Transient
    @SerializedName("label_uuids")
    private List<String> labelIds;


    @Transient
    @SerializedName("storage")
    private List<StorageItems> storageItems;

    @Transient
    @SerializedName("locales")
    private List<Locales> locales;

    @Transient
    @SerializedName("ingredients")
    private List<RecipeIngredients> recipeIngredients;

    @Transient
    private List<Modifier> modifiers;

    @Transient
    @SerializedName("recipe_tasks")
    private List<Task> tasks;


    @Transient
    public List<RecipeTag> tags;

    @Generated(hash = 146789254)
    public Recipe(String id, String name, String printerName, String description,
            float outputQuantity, String color, String outputUnitId, String roomId,
            String storageId, String rackId, String placeId, String imageUrl,
            Date createdAt, Date updatedAt, String recipeCategoryId, String lookUp,
            int type, int status, int version, String productBarcode,
            String parentLabel, String taskIngredientComaSperatad, String supplier,
            String supplierName, int inventoryType) {
        this.id = id;
        this.name = name;
        this.printerName = printerName;
        this.description = description;
        this.outputQuantity = outputQuantity;
        this.color = color;
        this.outputUnitId = outputUnitId;
        this.roomId = roomId;
        this.storageId = storageId;
        this.rackId = rackId;
        this.placeId = placeId;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.recipeCategoryId = recipeCategoryId;
        this.lookUp = lookUp;
        this.type = type;
        this.status = status;
        this.version = version;
        this.productBarcode = productBarcode;
        this.parentLabel = parentLabel;
        this.taskIngredientComaSperatad = taskIngredientComaSperatad;
        this.supplier = supplier;
        this.supplierName = supplierName;
        this.inventoryType = inventoryType;
    }

    @Generated(hash = 829032493)
    public Recipe() {
    }


    public List<String> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(List<String> labelIds) {
        this.labelIds = labelIds;
    }

    public List<RecipeIngredients> getRecipeIngredients() {
        return recipeIngredients;
    }

    public List<Locales> getLocales() {
        return locales;
    }

    public void setLocales(List<Locales> locales) {
        this.locales = locales;
    }

    public void setRecipeIngredients(List<RecipeIngredients> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<StorageItems> getStorageItems() {
        return storageItems;
    }

    public void setStorageItems(List<StorageItems> storageItems) {
        this.storageItems = storageItems;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrinterName() {
        return this.printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getOutputQuantity() {
        return this.outputQuantity;
    }

    public void setOutputQuantity(float outputQuantity) {
        this.outputQuantity = outputQuantity;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOutputUnitId() {
        return this.outputUnitId;
    }

    public void setOutputUnitId(String outputUnitId) {
        this.outputUnitId = outputUnitId;
    }

    public String getStorageId() {
        return this.storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public String getRackId() {
        return this.rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public String getPlaceId() {
        return this.placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRecipeCategoryId() {
        return this.recipeCategoryId;
    }

    public void setRecipeCategoryId(String recipeCategoryId) {
        this.recipeCategoryId = recipeCategoryId;
    }

    public String getLookUp() {
        return this.lookUp;
    }

    public void setLookUp(String lookUp) {
        this.lookUp = lookUp;
    }


    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getParentLabel() {
        return this.parentLabel;
    }

    public void setParentLabel(String parentLabel) {
        this.parentLabel = parentLabel;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Units getOutputUnit() {
        if (outputUnits == null) {
            outputUnits = ServiceLocator
                    .getInstance()
                    .getDatabaseAdapter()
                    .getUnitById(outputUnitId);
        }
        return outputUnits;
    }

    public void setOutputUnit(Units outputUnit) {
        this.outputUnits = outputUnit;
        if (outputUnit != null) {
            this.outputUnitId = outputUnit.getId();
        }
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(int inventoryType) {
        this.inventoryType = inventoryType;
    }

    public String getSupplierName() {
        return supplierName;
    }

//    public void setManaged(int managed) {
//        this.managed = managed;
//    }
//
//    public int getVersion() {
//        return version;
//    }
//
//    public void setVersion(int version) {
//        this.version = version;
//    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }
}
