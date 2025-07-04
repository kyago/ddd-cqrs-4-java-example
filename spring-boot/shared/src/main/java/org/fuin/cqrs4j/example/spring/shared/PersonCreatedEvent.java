package org.fuin.cqrs4j.example.spring.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.fuin.ddd4j.jackson.AbstractDomainEvent;
import org.fuin.ddd4j.core.AggregateVersion;
import org.fuin.ddd4j.core.EventType;
import org.fuin.esc.api.HasSerializedDataTypeConstant;
import org.fuin.esc.api.SerializedDataType;

import javax.annotation.concurrent.Immutable;

/**
 * A new person was created in the system.
 */
@Immutable
@HasSerializedDataTypeConstant
public final class PersonCreatedEvent extends AbstractDomainEvent<PersonId> {

    private static final long serialVersionUID = 1000L;

    /**
     * Never changing unique event type name.
     */
    public static final EventType TYPE = new EventType("PersonCreatedEvent");

    /**
     * Unique name used for marshalling/unmarshalling the event.
     */
    public static final SerializedDataType SER_TYPE = new SerializedDataType(PersonCreatedEvent.TYPE.asBaseType());

    @NotNull
    @JsonProperty("name")
    private PersonName name;

    /**
     * Protected default constructor for deserialization.
     */
    protected PersonCreatedEvent() {
        super();
    }

    @Override
    @JsonIgnore
    public final EventType getEventType() {
        return PersonCreatedEvent.TYPE;
    }

    /**
     * Returns: Name of a person.
     *
     * @return Current value.
     */
    @NotNull
    @JsonIgnore
    public final PersonName getName() {
        return name;
    }

    @Override
    public final String toString() {
        return "Person '" + name + "' (" + getEntityId() + ") was created [Event " + getEventId() + "]";
    }

    /**
     * Builds an instance of the outer class.
     */
    public static final class Builder extends AbstractDomainEvent.Builder<PersonId, PersonCreatedEvent, Builder> {

        private PersonCreatedEvent delegate;

        public Builder() {
            super(new PersonCreatedEvent());
            delegate = delegate();
        }

        public Builder id(PersonId personId) {
            entityIdPath(personId);
            return this;
        }

        public Builder name(String name) {
            delegate.name = new PersonName(name);
            return this;
        }

        public Builder name(PersonName name) {
            delegate.name = name;
            return this;
        }

        public Builder version(int version) {
            aggregateVersion(AggregateVersion.valueOf(version));
            return this;
        }

        public PersonCreatedEvent build() {
            ensureBuildableAbstractDomainEvent();
            ensureNotNull("name", delegate.name);

            final PersonCreatedEvent result = delegate;
            delegate = new PersonCreatedEvent();
            resetAbstractDomainEvent(delegate);
            return result;
        }

    }

}
