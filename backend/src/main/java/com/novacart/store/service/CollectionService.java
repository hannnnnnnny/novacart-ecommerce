package com.novacart.store.service;

import com.novacart.store.dto.CollectionRequest;
import com.novacart.store.dto.CollectionResponse;
import java.util.List;

public interface CollectionService {

    List<CollectionResponse> findPublicCollections();

    List<CollectionResponse> findFeaturedCollections();

    List<CollectionResponse> findAdminCollections();

    CollectionResponse createCollection(CollectionRequest request);

    CollectionResponse updateCollection(Long id, CollectionRequest request);

    void deleteCollection(Long id);
}
