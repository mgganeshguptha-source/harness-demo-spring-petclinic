/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OwnerTests {

	@Test
	void addPetAddsPersistedPet() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(5);
		pet.setName("Buddy");

		owner.addPet(pet);

		assertTrue(owner.getPets().contains(pet));
		assertEquals(1, owner.getPets().size());
	}

	@Test
	void addPetDoesNotAddDuplicatePet() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(5);
		pet.setName("Buddy");

		owner.addPet(pet);
		owner.addPet(pet);

		assertEquals(1, owner.getPets().size());
	}

	@Test
	void hasPetsReturnsFalseWhenOwnerHasNoPets() {
		Owner owner = new Owner();

		assertFalse(owner.hasPets());
		assertTrue(owner.getPets().isEmpty());
	}

	@Test
	void hasPetsReturnsTrueWhenOwnerHasOnePet() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(5);
		pet.setName("Buddy");

		owner.addPet(pet);

		assertTrue(owner.hasPets());
		assertEquals(1, owner.getPets().size());
		assertTrue(owner.getPets().contains(pet));
	}

	@Test
	void hasPetsReturnsTrueWhenOwnerHasMultiplePets() {
		Owner owner = new Owner();
		Pet firstPet = new Pet();
		firstPet.setId(5);
		firstPet.setName("Buddy");
		Pet secondPet = new Pet();
		secondPet.setId(6);
		secondPet.setName("Max");

		owner.addPet(firstPet);
		owner.addPet(secondPet);

		assertTrue(owner.hasPets());
		assertEquals(2, owner.getPets().size());
		assertEquals(firstPet, owner.getPets().get(0));
		assertEquals(secondPet, owner.getPets().get(1));
	}

	@Test
	void hasPetsDoesNotChangePetCollectionContentsOrSize() {
		Owner owner = new Owner();
		Pet firstPet = new Pet();
		firstPet.setId(5);
		firstPet.setName("Buddy");
		Pet secondPet = new Pet();
		secondPet.setId(6);
		secondPet.setName("Max");
		owner.addPet(firstPet);
		owner.addPet(secondPet);

		int petsSizeBeforeCheck = owner.getPets().size();

		assertTrue(owner.hasPets());
		assertEquals(petsSizeBeforeCheck, owner.getPets().size());
		assertEquals(firstPet, owner.getPets().get(0));
		assertEquals(secondPet, owner.getPets().get(1));
	}

	@Test
	void addPetWithNullRemainsNoOpAndHasPetsStaysFalse() {
		Owner owner = new Owner();

		owner.addPet(null);

		assertFalse(owner.hasPets());
		assertTrue(owner.getPets().isEmpty());
	}

	@Test
	void addPetRejectsDuplicatePersistedPetAndHasPetsStaysTrue() {
		Owner owner = new Owner();
		Pet firstPet = new Pet();
		firstPet.setId(5);
		firstPet.setName("Buddy");
		Pet duplicatePet = new Pet();
		duplicatePet.setId(5);
		duplicatePet.setName("Buddy");

		owner.addPet(firstPet);
		owner.addPet(duplicatePet);

		assertTrue(owner.hasPets());
		assertEquals(1, owner.getPets().size());
		assertTrue(owner.getPets().contains(firstPet));
	}

}
