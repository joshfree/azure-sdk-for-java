// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos;

import com.azure.core.annotation.ServiceClient;
import com.azure.core.util.IterableStream;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

/**
 * Provides a client-side logical representation of the Azure Cosmos database service.
 * SyncClient is used to perform operations in a synchronous way
 */
@ServiceClient(builder = CosmosClientBuilder.class)
public class CosmosClient implements AutoCloseable {
    private final CosmosAsyncClient asyncClientWrapper;

    CosmosClient(CosmosClientBuilder builder) {
        this.asyncClientWrapper = builder.buildAsyncClient();
    }

    /**
     * Instantiate the cosmos client builder to build cosmos client
     *
     * @return {@link CosmosClientBuilder}
     */
    public static CosmosClientBuilder cosmosClientBuilder() {
        return new CosmosClientBuilder();
    }

    /**
     * Create a Database if it does not already exist on the service
     *
     * @param databaseProperties {@link CosmosDatabaseProperties} the database properties
     * @return the {@link CosmosDatabaseResponse} with the created database.
     * @throws CosmosClientException the cosmos client exception.
     */
    public CosmosDatabaseResponse createDatabaseIfNotExists(CosmosDatabaseProperties databaseProperties) throws
        CosmosClientException {
        return mapDatabaseResponseAndBlock(asyncClientWrapper.createDatabaseIfNotExists(databaseProperties));
    }

    /**
     * Create a Database if it does not already exist on the service
     *
     * @param id the id of the database
     * @return the {@link CosmosDatabaseResponse} with the created database.
     * @throws CosmosClientException the cosmos client exception.
     */
    public CosmosDatabaseResponse createDatabaseIfNotExists(String id) throws CosmosClientException {
        return mapDatabaseResponseAndBlock(asyncClientWrapper.createDatabaseIfNotExists(id));
    }


    /**
     * Creates a database.
     *
     * @param databaseProperties {@link CosmosDatabaseProperties} the database properties.
     * @param options the request options.
     * @return the {@link CosmosDatabaseResponse} with the created database.
     * @throws CosmosClientException the cosmos client exception.
     */
    public CosmosDatabaseResponse createDatabase(CosmosDatabaseProperties databaseProperties,
                                                 CosmosDatabaseRequestOptions options) throws CosmosClientException {
        return mapDatabaseResponseAndBlock(asyncClientWrapper.createDatabase(databaseProperties, options));
    }

    /**
     * Creates a database.
     *
     * @param databaseProperties {@link CosmosDatabaseProperties} the database properties.
     * @return the {@link CosmosDatabaseResponse} with the created database.
     * @throws CosmosClientException the cosmos client exception.
     */
    public CosmosDatabaseResponse createDatabase(CosmosDatabaseProperties databaseProperties) throws
        CosmosClientException {
        return mapDatabaseResponseAndBlock(asyncClientWrapper.createDatabase(databaseProperties));
    }

    /**
     * Creates a database.
     *
     * @param id the id of the database
     * @return the {@link CosmosDatabaseResponse} with the created database.
     * @throws CosmosClientException the cosmos client exception.
     */
    public CosmosDatabaseResponse createDatabase(String id) throws CosmosClientException {
        return mapDatabaseResponseAndBlock(asyncClientWrapper.createDatabase(id));

    }

    /**
     * Creates a database.
     *
     * @param databaseProperties {@link CosmosDatabaseProperties} the database properties.
     * @param throughput the throughput
     * @param options {@link CosmosDatabaseRequestOptions} the request options
     * @return the {@link CosmosDatabaseResponse} with the created database.
     * @throws CosmosClientException the cosmos client exception
     */
    public CosmosDatabaseResponse createDatabase(CosmosDatabaseProperties databaseProperties,
                                                 int throughput,
                                                 CosmosDatabaseRequestOptions options) throws CosmosClientException {
        return mapDatabaseResponseAndBlock(asyncClientWrapper.createDatabase(databaseProperties, throughput, options));
    }

    /**
     * Creates a database.
     *
     * @param databaseProperties {@link CosmosDatabaseProperties} the database properties.
     * @param throughput the throughput
     * @return the {@link CosmosDatabaseResponse} with the created database.
     * @throws CosmosClientException the cosmos client exception
     */
    public CosmosDatabaseResponse createDatabase(CosmosDatabaseProperties databaseProperties,
                                                 int throughput) throws CosmosClientException {
        return mapDatabaseResponseAndBlock(asyncClientWrapper.createDatabase(databaseProperties, throughput));
    }


    /**
     * Creates a database.
     *
     * @param id the id of the database
     * @param throughput the throughput
     * @return the {@link CosmosDatabaseResponse} with the created database.
     * @throws CosmosClientException the cosmos client exception
     */
    public CosmosDatabaseResponse createDatabase(String id, int throughput) throws CosmosClientException {
        return mapDatabaseResponseAndBlock(asyncClientWrapper.createDatabase(id, throughput));
    }

    CosmosDatabaseResponse mapDatabaseResponseAndBlock(Mono<CosmosAsyncDatabaseResponse> databaseMono) throws
        CosmosClientException {
        try {
            return databaseMono
                       .map(this::convertResponse)
                       .block();
        } catch (Exception ex) {
            final Throwable throwable = Exceptions.unwrap(ex);
            if (throwable instanceof CosmosClientException) {
                throw (CosmosClientException) throwable;
            } else {
                throw Exceptions.propagate(ex);
            }
        }
    }

    /**
     * Reads all databases.
     *
     * @param options {@link FeedOptions}the feed options.
     * @return the {@link IterableStream} for feed response with the read databases.
     */
    public IterableStream<FeedResponse<CosmosDatabaseProperties>> readAllDatabases(FeedOptions options) {
        return getFeedIterableStream(asyncClientWrapper.readAllDatabases(options));
    }

    /**
     * Reads all databases.
     *
     * @return the {@link IterableStream} for feed response with the read databases.
     */
    public IterableStream<FeedResponse<CosmosDatabaseProperties>> readAllDatabases() {
        return getFeedIterableStream(asyncClientWrapper.readAllDatabases());
    }

    /**
     * Query a database
     *
     * @param query the query
     * @param options {@link FeedOptions}the feed options.
     * @return the {@link IterableStream} for feed response with the obtained databases.
     */
    public IterableStream<FeedResponse<CosmosDatabaseProperties>> queryDatabases(String query, FeedOptions options) {
        return getFeedIterableStream(asyncClientWrapper.queryDatabases(query, options));
    }

    /**
     * Query a database
     *
     * @param querySpec {@link SqlQuerySpec} the query spec
     * @param options the query
     * @return the {@link IterableStream} for feed response with the obtained databases.
     */
    public IterableStream<FeedResponse<CosmosDatabaseProperties>> queryDatabases(SqlQuerySpec querySpec,
                                                                    FeedOptions options) {
        return getFeedIterableStream(asyncClientWrapper.queryDatabases(querySpec, options));
    }

    /**
     * Gets the database client
     *
     * @param id the id of the database
     * @return {@link CosmosDatabase} the cosmos sync database
     */
    public CosmosDatabase getDatabase(String id) {
        return new CosmosDatabase(id, this, asyncClientWrapper.getDatabase(id));
    }

    CosmosDatabaseResponse convertResponse(CosmosAsyncDatabaseResponse response) {
        return new CosmosDatabaseResponse(response, this);
    }

    CosmosAsyncClient asyncClient() {
        return this.asyncClientWrapper;
    }

    /**
     * Close this {@link CosmosClient} instance
     */
    public void close() {
        asyncClientWrapper.close();
    }

    private <T> IterableStream<FeedResponse<T>> getFeedIterableStream(CosmosContinuablePagedFlux<T> cosmosContinuablePagedFlux) {
        return IterableStream.of(cosmosContinuablePagedFlux.byPage().toIterable());
    }

}